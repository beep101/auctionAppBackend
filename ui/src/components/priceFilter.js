import React, { useCallback, useEffect, useRef, useState } from 'react'
import { Slider, Rail, Handles, Tracks } from 'react-compound-slider';
import { getPriceHistogram } from '../apiConsumer/itemFetchConsumer';


//add track
//do something with css???
//use values and percentage to programatically position handles???
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
        //reset only one value and keep the other one
        //update slider
        if(props.resetMin){
            props.rangeSet(null,null);
            setSelectedMin(null);
        }
        if(props.resetMax){
            props.rangeSet(null,null);
            setSelectedMax(null);
        }
    },[props.resetMin,props.resetMax])

    const onChange=(data)=>{
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
                </Slider>
            </div>
            <div className="priceFilterText">{selectedMin?`$${selectedMin}`:'Min'} - {selectedMax?`$${selectedMax}`:'Max'}</div>
            <div className="priceFilterText">The average price is ${avgPrice}</div>
        </div>
    )
}

export default PriceFilter
