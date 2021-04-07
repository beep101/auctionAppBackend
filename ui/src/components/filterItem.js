import React from 'react';

function FilterItem(props){
    console.log(props)
    return(
        <div onClick={()=>props.disable(props.filter.type)} className="activeFilter">
            <span className="activeFilterLabel">{props.filter.value}</span>
            <img className="filterButtonIcon" src="/images/x_icon.svg"/>
        </div>
    )
}

export default FilterItem