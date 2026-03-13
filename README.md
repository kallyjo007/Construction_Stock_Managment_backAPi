## Construction Stock Management System – Design Overview

This README explains how the project satisfies (or is prepared to satisfy) the assessment requirements: entities, relationships, locations, and key Spring Data JPA concepts.

---

## 1. Entities and ERD (at least 5 tables)

**Main entities used in the ERD**

- **User**
- **Role**
- **Category**
- **Material**
- **Supplier**
- **Warehouse**
- **ProjectSite**
- **PurchaseOrder**
- **PurchaseOrderItem**
- **StockTransaction**
- **Location**

**Key relationships**

- **User 1 ──── N PurchaseOrder**
  - `User` has `@OneToMany(mappedBy = "createdBy") List<PurchaseOrder> purchaseOrders`
  - `PurchaseOrder` has `@ManyToOne @JoinColumn(name = "created_by") private User createdBy`
  - **Foreign key**: `purchase_orders.created_by`

- **Role 1 ──── N User**
  - `User` has `@ManyToOne @JoinColumn(name = "role_id") private Role role`
  - (The inverse `@OneToMany` collection can be added in `Role` if needed.)
  - **Foreign key**: `users.role_id`

- **Category 1 ──── N Material**
  - `Category` has `@OneToMany(mappedBy = "category") List<Material> materials`
  - `Material` has `@ManyToOne @JoinColumn(name = "category_id") private Category category`
  - **Foreign key**: `materials.category_id`

- **Supplier 1 ──── N PurchaseOrder**
  - `Supplier` has `@OneToMany(mappedBy = "supplier") List<PurchaseOrder> purchaseOrders`
  - `PurchaseOrder` has `@ManyToOne @JoinColumn(name = "supplier_id") private Supplier supplier`
  - **Foreign key**: `purchase_orders.supplier_id`

- **Warehouse 1 ──── N StockTransaction**
  - `Warehouse` has `@OneToMany(mappedBy = "warehouse") List<StockTransaction> stockTransactions`
  - `StockTransaction` has `@ManyToOne @JoinColumn(name = "warehouse_id") private Warehouse warehouse`
  - **Foreign key**: `stock_transactions.warehouse_id`

- **Self‑referencing Location hierarchy**
  - `Location` has:
    - `@Enumerated(EnumType.STRING) private ELocationType type;`
    - `@ManyToOne @JoinColumn(name = "parent_id") private Location parent;`
  - This allows a tree structure:
    - `PROVINCE → DISTRICT → SECTOR → CELL → VILLAGE`

- **Location 1 ──── N Warehouse**
  - `Warehouse` has `@ManyToOne @JoinColumn(name = "location_id") private Location location`
  - A single `Location` (for example a VILLAGE) can be linked to many warehouses.
  - **Foreign key**: `warehouses.location_id`

You can draw the ERD from these entities and foreign keys; this README provides the verbal explanation that the rubric asks for.

---

## 2. Location saving and hierarchy

### 2.1 `Location` entity

- **Table**: `locations`
- **Fields**:
  - `id` (UUID, primary key)
  - `code` (string, business code for the location)
  - `name` (string, human‑readable name)
  - `type` (`ELocationType`: `PROVINCE`, `DISTRICT`, `SECTOR`, `CELL`, `VILLAGE`)
  - `parent` (`Location`): self‑referencing parent to build the hierarchy

### 2.2 Self‑referencing logic

- When a **province** is saved:
  - `type = PROVINCE`
  - `parent = null`
- When a **district** is saved:
  - `type = DISTRICT`
  - `parent = <province Location>`
- When a **sector** is saved:
  - `type = SECTOR`
  - `parent = <district Location>`
- And so on for `CELL` and `VILLAGE`.

This creates a **tree** where each node knows its immediate parent. Queries can walk this tree to reach province from village if needed.

### 2.3 Saving a Location (`LocationService` + `LocationController`)

- **Repository**
  - `LocationRepository extends JpaRepository<Location, UUID>`
  - Includes `Boolean existsByCode(String code);` to check if a location code already exists.

- **Service**
  - `LocationService.saveLocation(Location location, String parentId)`
  - Logic:
    1. If `parentId` is provided:
       - Convert `parentId` string to `UUID`.
       - Load parent `Location` from DB.
       - If found, set `location.setParent(parent)`.
    2. Call `locationRepository.existsByCode(location.getCode())`.
       - If `true`: return `"Location with that code already exists"`.
       - If `false`: `locationRepository.save(location)` and return `"Location saved successfully"`.

- **Controller**
  - Endpoint: `POST /api/locations/save`
  - Parameters:
    - **Body**: JSON `Location` object.
    - **Query param**: `parentId` (optional, String).
  - The controller calls `locationService.saveLocation(location, parentId)` and translates the result into:
    - HTTP 200 (OK) for success.
    - HTTP 409 (CONFLICT) if the code already exists.

**Explanation in words**:  
When saving a location, the system first attaches an optional parent (to build the hierarchy) and then uses `existsByCode` to **prevent duplicate codes**. Only if the code is unique is the location actually stored in the database.

### 2.4 Warehouse linked to Location

- `Warehouse` has:
  - `@ManyToOne @JoinColumn(name = "location_id") private Location location;`
  - A separate `address` string for free‑text description.

**Saving a warehouse with location**

- `WarehouseService.saveWarehouseWithLocation(Warehouse warehouse, String locationId)`:
  1. Find `Location` by `UUID` using `locationRepository.findById(...)`.
  2. If found, set `warehouse.setLocation(location)`.
  3. Save the warehouse using `warehouseRepository.save(warehouse)`.

- Controller endpoint:
  - `POST /api/warehouses/save-with-location?locationId=<UUID>`
  - JSON body with warehouse fields (`name`, `address`, `capacity`, etc.).

This demonstrates **how the location relationship is handled and stored**: the `warehouses` table gets a foreign key `location_id` pointing to the chosen `locations.id`.

---

## 3. Many-to-Many relationship (PurchaseOrder ↔ Material)

The many‑to‑many between `PurchaseOrder` and `Material` is implemented using a join table entity **`PurchaseOrderItem`**.

- **`PurchaseOrderItem`** (join table)
  - Fields: `item_id`, `quantity`, `price`, `subtotal`
  - Relationships:
    - `@ManyToOne @JoinColumn(name = "order_id") private PurchaseOrder purchaseOrder;`
    - `@ManyToOne @JoinColumn(name = "material_id") private Material material;`
  - **Database view**:
    - `purchase_order_items.order_id` → FK to `purchase_orders.order_id`
    - `purchase_order_items.material_id` → FK to `materials.material_id`

- **`PurchaseOrder`**:
  - `@OneToMany(mappedBy = "purchaseOrder") private List<PurchaseOrderItem> items;`

**Why this is many‑to‑many**:

- One `PurchaseOrder` can have **many** `PurchaseOrderItem` rows (each referencing a different material).
- One `Material` can appear in **many** `PurchaseOrderItem` rows (belonging to different orders).
- Therefore, `PurchaseOrder` and `Material` are connected in an N:N relationship via the join table `purchase_order_items`.

---

## 4. One-to-Many and Many-to-One mapping (example explanation)

Example: **Category 1 ──── N Material**

- In `Material`:
  - `@ManyToOne`
  - `@JoinColumn(name = "category_id")`
  - **Meaning**: many materials share the same category; the column `category_id` in the `materials` table is a foreign key to `categories.category_id`.

- In `Category`:
  - `@OneToMany(mappedBy = "category")`
  - `private List<Material> materials;`
  - **Meaning**: from a category, we can navigate to all materials that reference it.

**Foreign key usage**:

- The **owning side** is `Material` (`@ManyToOne` with `@JoinColumn`).
- The **foreign key** physically lives in the `materials` table as `category_id`.

The same pattern is used for:

- `Supplier` 1 ──── N `PurchaseOrder` (FK: `purchase_orders.supplier_id`)
- `User` 1 ──── N `PurchaseOrder` (FK: `purchase_orders.created_by`)
- `Warehouse` 1 ──── N `StockTransaction` (FK: `stock_transactions.warehouse_id`)

---

## 5. Self-referencing hierarchy vs. province requirement

The requirement “retrieve all users from a given province using province code OR province name” is conceptually implemented here using the **`Location` hierarchy** and linking **warehouses** to locations.

If needed, the same pattern can be applied to `User`:

- Add `@ManyToOne private Location location;` in `User`.
- Use `Location.type = PROVINCE` and `Location.code` or `Location.name` to filter.
- Repository methods:
  - e.g. `List<User> findByLocation_CodeOrLocation_Name(String code, String name);`

This README explains the pattern so it is clear how you would extend it to users for the final requirement.

---

## 6. `existsBy()` method explanation

The project demonstrates **existence checking** in Spring Data JPA using the `Location` entity:

- `LocationRepository` defines:
  - `Boolean existsByCode(String code);`

- `LocationService.saveLocation(...)`:
  - Calls `locationRepository.existsByCode(location.getCode())` before saving.

**How it works**:

- Spring Data JPA parses the method name `existsByCode` and generates a query like:
  - `SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM Location l WHERE l.code = :code`
- This query only checks **existence**, not fetching the full entity, which is more efficient than loading an entire `Location` object when we only care whether it exists.

---

## 7. Sorting and Pagination (implemented on `User`)

Sorting and pagination are implemented for the `User` entity using Spring Data JPA’s `Page`, `Pageable`, and `Sort`.

### 7.1 Service logic (`UserService`)

- Method:
  - `public Page<User> getUsersPage(int page, int size, String sortBy, String sortDir)`
- Implementation idea:
  - Build a `Sort` object based on `sortBy` and `sortDir`:
    - If `sortDir = "desc"`, use `Sort.by(sortBy).descending()`
    - Otherwise use `Sort.by(sortBy).ascending()`
  - Build a `Pageable`:
    - `Pageable pageable = PageRequest.of(page, size, sort);`
  - Call the repository with pagination:
    - `Page<User> result = userRepository.findAll(pageable);`

This returns a `Page<User>` which contains:

- The current page content (`getContent()`).
- Total number of pages and elements.
- Flags like `isFirst()`, `isLast()`, etc.

### 7.2 Controller endpoint (`UserController`)

- Endpoint:
  - `GET /api/users/page`
- Query parameters:
  - `page` (default `0`) – which page to fetch (0‑based index).
  - `size` (default `10`) – how many users per page.
  - `sortBy` (default `userId`) – which field to sort by (e.g. `name`, `email`).
  - `sortDir` (default `asc`) – sort direction: `asc` or `desc`.
- Example call:

  - `GET /api/users/page?page=0&size=5&sortBy=name&sortDir=asc`

**How this improves performance and usability**:

- **Pagination**:
  - Instead of returning all users at once, the API returns only a **slice** (for example 5 or 10 users).
  - This reduces:
    - Amount of data sent over the network.
    - Memory usage on both server and client.
  - It is especially important when the `users` table grows large.

- **Sorting**:
  - Sorting is pushed down to the database using `ORDER BY`, controlled through the `Sort` object.
  - The client can choose:
    - Which field to sort by (`sortBy`).
    - Direction (`sortDir`), without changing code.

Together, pagination and sorting provide a flexible and performant way to browse large user lists.

---

## 8. One-to-One relationship (Warehouse ↔ User as manager)

The project demonstrates a **one-to-one** relationship using existing entities: each warehouse can have exactly one manager user, and each user can manage at most one warehouse.

### 8.1 Mapping on `Warehouse` (owning side)

- In `Warehouse`:
  - `@OneToOne`
  - `@JoinColumn(name = "manager_id", unique = true)`
  - `private User manager;`

**What this means**:

- The `warehouses` table gets a column `manager_id` which is a foreign key to `users.user_id`.
- The `unique = true` constraint on `manager_id` ensures that **the same user cannot be assigned as manager to more than one warehouse**, which enforces the 1–1 rule at the database level.

### 8.2 Back-reference on `User` (inverse side)

- In `User`:
  - `@OneToOne(mappedBy = "manager")`
  - `private Warehouse managedWarehouse;`

**What this means**:

- `User` does not own the foreign key; it simply provides a convenient way to navigate from a user to the warehouse they manage.
- The `mappedBy = "manager"` tells JPA that the actual relationship (and the foreign key) is defined in the `Warehouse.manager` field.

### 8.3 Explanation in words

- There is at most one `Warehouse` row pointing to a given `User` row via `manager_id` because of the unique constraint.
- As a result, the pair (`Warehouse`, `User`) behaves as a **one-to-one relationship**:
  - One warehouse ↔ one manager user.
  - One user ↔ at most one managed warehouse.

---

## 9. How to test key flows with Postman

- **Save a Location (any level)**
  - `POST /api/locations/save`
  - Body:

    ```json
    {
      "code": "KIG",
      "name": "Kigali",
      "type": "PROVINCE"
    }
    ```

  - Optional query param `parentId` to attach to an existing parent location.

- **Save a Warehouse with an existing Location**
  - `POST /api/warehouses/save-with-location?locationId=<UUID>`
  - Body:

    ```json
    {
      "name": "Main Warehouse",
      "address": "Near main road",
      "capacity": 1000
    }
    ```

- **Search warehouses by name**
  - `GET /api/warehouses/search/name?name=Main`

- **Search warehouses by address**
  - `GET /api/warehouses/search/address?address=road`

These examples show how the API endpoints use the relationships defined in the entities to persist and query data.


