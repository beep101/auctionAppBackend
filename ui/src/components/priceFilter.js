import React, { useEffect, useRef, useState } from 'react'
import { Slider, Handles, Tracks } from 'react-compound-slider';
import { getPriceHistogram } from '../apiConsumer/itemFetchConsumer';

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

    const sliderStyle = {
        position: 'relative',
        width: '12vw'
      }
      
      const railStyle = {
        position: 'absolute',
        width: '100%',
        height: 3,
        marginTop: 6,
        backgroundColor: '#d8d8d8',
      }

      const trackStyle={
        position: 'absolute',
        height: 3,
        zIndex: 1,
        marginTop: 6,
        backgroundColor: '#8367D8',
      }
      
      const handleStyle={
        position: 'absolute',
        marginLeft: -8,
        marginTop: 0,
        zIndex: 2,
        width: 16,
        height: 16,
        cursor: 'pointer',
        borderRadius: '50%',
        backgroundColor: '#8367D8'
      }

    return(
        <div className="priceFilterContainer">
            <div className="filterTittle">Price Filter</div>
            <div className="priceSliderFilterContainer">
                <div className="histogramContainer">
                    {histogram.map(bar=>
                        <div className="histogramBar" style={{height:`${bar.count*heightPerCount.current}vw`}}></div>
                    )}
                </div>
                <Slider
                    rootStyle={sliderStyle}
                    domain={[0.0,upperBound]}
                    step={0.01}
                    mode={2}
                    values={[minRange,maxRange]}
                    onChange={onChange}
                >
                    <div style={railStyle}></div>
                    <Handles>
                    {({handles, getHandleProps}) => (
                        <div className="slider-handles">
                            {handles.map(handle=>(
                                <div
                                    style={{...handleStyle,...{left:`${handle.percent}%`}}}
                                    {...getHandleProps(handle.id)}
                                ></div>
                            ))}
                        </div>
                    )}
                    </Handles>
                    <Tracks left={false} right={false}>
                        {({ tracks }) => (
                            <div className="slider-tracks">
                                {tracks.map((track) => (
                                    <div
                                        style={{...trackStyle,...{
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
            <div className="priceFilterText">{selectedMin?`$${selectedMin}`:'Min'} - {selectedMax?`$${selectedMax}`:'Max'}</div>
            <div className="priceFilterText">The average price is ${avgPrice}</div>
        </div>
    )
}

export default PriceFilter
