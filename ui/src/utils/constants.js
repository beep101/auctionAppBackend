export const DEFAULT_TOAST_CONFIG={
    position: "top-center",
    autoClose: 3000,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: false,
    draggable: false,
    progress: 0,
}

export const SORTING_SELECT_THEME=(theme)=>({
    ...theme,
    borderRadius: 0,
    colors: {
        ...theme.colors,
        primary25: '#8367D8'
    },
})

export const SHOP_LOAD_COUNT=6;