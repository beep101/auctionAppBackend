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

export const SHORT_DESCRIPTION_CHAR_COUNT=150;

export const PHONE_REGEX_PATTERNS=["^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                          "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" ,
                          "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"];

export const ONE_DAY_MILIS=24*60*60*1000;