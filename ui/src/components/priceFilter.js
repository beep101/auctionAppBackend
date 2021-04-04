import React, { useEffect, useState } from 'react'
import { Slider, Rail, Handles, Tracks } from 'react-compound-slider';
import { getPriceHistogram } from '../apiConsumer/itemFetchConsumer';

function PriceFilter(props){

    const [maxRange,setMaxRange]=useState(100);
    const [histogram,setHistogram]=useState([]);

    useEffect(()=>{
        getPriceHistogram((success,data)=>{
            if(success){
                setMaxRange(data.histogram[data.histogram.length-1].upperBound);
                setHistogram(data.histogram);
            }
        })
    },[])

    const sliderStyle = {  // Give the slider some width
        position: 'relative',
        width: '100%',
        height: 80,
        border: '1px solid steelblue',
      }
      
      const railStyle = {
        position: 'absolute',
        width: '100%',
        height: 10,
        marginTop: 35,
        borderRadius: 5,
        backgroundColor: '#8B9CB6',
      }
      
      const handleStyle={
        position: 'absolute',
        marginLeft: -15,
        marginTop: 25,
        zIndex: 2,
        width: 30,
        height: 30,
        border: 0,
        textAlign: 'center',
        cursor: 'pointer',
        borderRadius: '50%',
        backgroundColor: '#2C4870',
        color: '#333',
      }

    const onChange=(data)=>{
        if(data[0]===0)
            data[0]=null;
        if(data[1]===maxRange)
            data[1]=null;
        props.rangeSet(data[0],data[1])
    }

    return(
        <div>
            <Slider
                rootStyle={sliderStyle}
                domain={[0.0, maxRange]}
                step={0.01}
                mode={2}
                values={[0,maxRange]}
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
    )
}

export default PriceFilter
