document.addEventListener("DOMContentLoaded", function () {
    const priceInput = document.getElementById("filterPrice");
    const quantityInput = document.getElementById("filterQuantity");
    const warehouseInput = document.getElementById("filterWarehouse");
    const categoryInput = document.getElementById("filterCategory");
    const barcodeInput = document.getElementById("filterBarcode");

    const filterTable = () => {
        const maxPrice = parseFloat(priceInput.value) || Infinity;
        const minQuantity = parseInt(quantityInput.value) || 0;
        const warehouseQuery = warehouseInput.value.toLowerCase();
        const categoryQuery = categoryInput.value.toLowerCase();
        const barcodeQuery = barcodeInput.value.toLowerCase();

        const rows = document.querySelectorAll("table tbody tr");

        rows.forEach(row => {
            const price = parseFloat(row.children[3].textContent) || 0;
            const quantity = parseInt(row.children[4].textContent) || 0;
            const barcode = row.children[5].textContent.toLowerCase();
            const category = row.children[6].textContent.toLowerCase();
            const warehouses = row.children[7].textContent.toLowerCase();

            const matchesPrice = price <= maxPrice;
            const matchesQuantity = quantity >= minQuantity;
            const matchesWarehouse = warehouses.includes(warehouseQuery);
            const matchesCategory = category.includes(categoryQuery);
            const matchesBarcode = barcode.includes(barcodeQuery);

            row.style.display =
                matchesPrice &&
                matchesQuantity &&
                matchesWarehouse &&
                matchesCategory &&
                matchesBarcode
                    ? ""
                    : "none";
        });
    };

    priceInput.addEventListener("input", filterTable);
    quantityInput.addEventListener("input", filterTable);
    warehouseInput.addEventListener("input", filterTable);
    categoryInput.addEventListener("input", filterTable);
    barcodeInput.addEventListener("input", filterTable);

    console.log("inventory-filters.js with category & barcode filtering is loaded!");
});
