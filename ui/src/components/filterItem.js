import React from 'react';

function FilterItem(props){

    return(
        <div onClick={props.disable} className="activeFilter">
            <span className="activeFilterLabel">{props.name}</span>
            <img className="filterButtonIcon" src="/images/x_icon.svg"/>
        </div>
    )
}

export default FilterItem