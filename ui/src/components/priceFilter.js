import React, { useEffect, useRef, useState } from 'react'
import { Slider, Handles, Tracks } from 'react-compound-slider';
import { getPriceHistogram } from '../apiConsumer/itemFetchConsumer';
import {PRICE_FILTER_SLIDER_STYLE,PRICE_FILTER_RAIL_STYLE,PRICE_FILTER_TRACK_STYLE,PRICE_FILTER_HANDLE_STYLE,PRICE_FILTER_HANDLE_CENTER_STYLE} from '../styles/priceFilterStyleConstants'

function PriceFilter(props){

    const heightPerCount=useRef(0);
    const [selectedMin, setSelectedMin]=useState(null);
    const [selectedMax, setSelectedMax]=useState(null);
    const [avgPrice,setAvgPrice]=useState(50);
    const [maxRange,setMaxRange]=useState(100);
    const [minRange,setMinRange]=useState(0);
    const [upperBound,setUpperBound]=useState(100);
    const [histogram,setHistogram]=useState([]);

    useEffect(()=>{
        getPriceHistogram((success,data)=>{
            if(success){
                setMaxRange(data.histogram[data.histogram.length-1].upperBound);
                setUpperBound(data.histogram[data.histogram.length-1].upperBound);
                setHistogram(data.histogram);
                const maxCount=data.histogram.reduce(function(prev, current){return prev.count > current.count? prev : current}).count;
                heightPerCount.current=4/maxCount;
                let total=0;
                let count=0;
                for(const h in data.histogram){
                    total+=data.histogram[h].count*data.histogram[h].upperBound;
                    count+=data.histogram[h].count;
                }
                setAvgPrice((total/count).toFixed(2))
            }
        })
    },[])

    useEffect(()=>{
        if(props.resetMin){
            if(maxRange!=upperBound)
                props.rangeSet(null,maxRange.toFixed(2));
            else
                props.rangeSet(null,null);
            setSelectedMin(null);
            setMinRange(0.0);
        }
        if(props.resetMax){
            if(minRange!=0.0)
                props.rangeSet(minRange.toFixed(2),null);
            else
                props.rangeSet(null,null);
            setSelectedMax(null);
            setMaxRange(upperBound);
        }
    },[props.resetMin,props.resetMax])

    const onChange=(data)=>{
        setMinRange(data[0]);
        setMaxRange(data[1]);
        if(data[0]===0)
            data[0]=null;
        else
            data[0]=data[0].toFixed(2);
        if(data[1]===upperBound)
            data[1]=null;
        else
            data[1]=data[1].toFixed(2);
        setSelectedMin(data[0]);
        setSelectedMax(data[1]);
        props.rangeSet(data[0],data[1])
    };

    return(
        <div className="priceFilterContainer">
            <div className="filterTittle">FILTER BY PRICE</div>
            <div className="priceSliderFilterContainer">
                <div className="histogramContainer">
                    {histogram.map(bar=>
                        <div className="histogramBar" style={{height:`${bar.count*heightPerCount.current}vw`}}></div>
                    )}
                </div>
                <Slider
                    rootStyle={PRICE_FILTER_SLIDER_STYLE}
                    domain={[0.0,upperBound]}
                    step={0.01}
                    mode={2}
                    values={[minRange,maxRange]}
                    onChange={onChange}
                >
                    <div style={PRICE_FILTER_RAIL_STYLE}></div>
                    <Handles>
                    {({handles, getHandleProps}) => (
                        <div className="slider-handles">
                            {handles.map(handle=>(
                                <div
                                    style={{...PRICE_FILTER_HANDLE_STYLE,...{left:`${handle.percent}%`}}}
                                    {...getHandleProps(handle.id)}
                                >
                                    <div style={PRICE_FILTER_HANDLE_CENTER_STYLE}>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                    </Handles>
                    <Tracks left={false} right={false}>
                        {({ tracks }) => (
                            <div className="slider-tracks">
                                {tracks.map((track) => (
                                    <div
                                        style={{...PRICE_FILTER_TRACK_STYLE,...{
                                            left: `${track.source.percent}%`,
                                            width: `${track.target.percent - track.source.percent}%`,
                                        }}}
                                    ></div>
                                ))}
                            </div>
                        )}
                    </Tracks>
                </Slider>
            </div>
            <div className="priceFilterTextContainer">
                <div className="priceFilterText">{selectedMin?`$${selectedMin}`:'Min'} - {selectedMax?`$${selectedMax}`:'Max'}</div>
                <div className="priceFilterText">The average price is ${avgPrice}</div>
            </div>
        </div>
    )
}

export default PriceFilter
