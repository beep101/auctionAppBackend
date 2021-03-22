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
        primary25: '#8367D8',
        primary: '#8367D8',
        primary75: "#5d5d5d",
        neutral10: "#5d5d5d"
    },
})

export const SORTING_SELECT_STYLES = {
    control: base => ({
      ...base,
      fontFamily: "'Lato', sans-serif"
    }),
    menu: base => ({
      ...base,
      fontFamily: "'Lato', sans-serif"
    })
  };

export const SHOP_LOAD_COUNT=6;

export const ITEM_MSG_DELAY= 6000;