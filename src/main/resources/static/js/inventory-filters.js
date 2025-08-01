document.addEventListener("DOMContentLoaded", function () {
    const priceInput = document.getElementById("filterPrice");
    const quantityInput = document.getElementById("filterQuantity");
    const warehouseInput = document.getElementById("filterWarehouse");

    const filterTable = () => {
        const maxPrice = parseFloat(priceInput.value) || Infinity;
        const minQuantity = parseInt(quantityInput.value) || 0;
        const warehouseQuery = warehouseInput.value.toLowerCase();

        const rows = document.querySelectorAll("table tbody tr");

        rows.forEach(row => {
            const price = parseFloat(row.children[3].textContent) || 0;
            const quantity = parseInt(row.children[4].textContent) || 0;
            const warehouses = row.children[5].textContent.toLowerCase();

            const matchesPrice = price <= maxPrice;
            const matchesQuantity = quantity >= minQuantity;
            const matchesWarehouse = warehouses.includes(warehouseQuery);

            row.style.display = (matchesPrice && matchesQuantity && matchesWarehouse) ? "" : "none";
        });
    };
    console.log("inventory-filters.js is loaded!");
    priceInput.addEventListener("input", filterTable);
    quantityInput.addEventListener("input", filterTable);
    warehouseInput.addEventListener("input", filterTable);
});
