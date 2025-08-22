(function () {
    function togglePair(modeNew, blockExisting, blockNew, selectExisting, countryNew) {
        const useNew = modeNew && modeNew.checked;
        if (!blockExisting || !blockNew) return;
        blockExisting.classList.toggle('d-none', useNew);
        blockNew.classList.toggle('d-none', !useNew);
        if (selectExisting) selectExisting.toggleAttribute('required', !useNew);
        if (countryNew) countryNew.toggleAttribute('required', useNew);
    }

    function attach(mExisting, mNew, bExisting, bNew, sExisting, cNew) {
        if (!mExisting && !mNew) return;
        if (mExisting) mExisting.addEventListener('change', () =>
            togglePair(mNew, bExisting, bNew, sExisting, cNew)
        );
        if (mNew) mNew.addEventListener('change', () =>
            togglePair(mNew, bExisting, bNew, sExisting, cNew)
        );
        togglePair(mNew, bExisting, bNew, sExisting, cNew);
    }

    function init() {
        attach(
            document.getElementById('modeExistingEdit'),
            document.getElementById('modeNewEdit'),
            document.getElementById('existingRateBlockEdit'),
            document.getElementById('newRateBlockEdit'),
            document.getElementById('existingTaxRateIdEdit'),
            document.getElementById('newRateCountryEdit')
        );

        attach(
            document.getElementById('modeExistingCreate'),
            document.getElementById('modeNewCreate'),
            document.getElementById('existingRateBlockCreate'),
            document.getElementById('newRateBlockCreate'),
            document.getElementById('existingTaxRateIdCreate'),
            document.getElementById('newRateCountryCreate')
        );
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
