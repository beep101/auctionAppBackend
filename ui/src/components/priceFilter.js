import React, { useCallback, useEffect, useState } from 'react'
import { Slider, Rail, Handles, Tracks } from 'react-compound-slider';
import { getPriceHistogram } from '../apiConsumer/itemFetchConsumer';

function PriceFilter(props){

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
            }
        })
    },[])

    useEffect(()=>{
        if(props.resetMin){
            console.log('reset min')
            props.rangeSet(null,undefined);
        }
        if(props.resetMax){
            console.log('reset max')
            props.rangeSet(undefined,null);
        }
    },[props.resetMin,props.resetMax])

    const onChange=useCallback((data)=>{
        if(data[0]===0)
            data[0]=null;
        if(data[1]===upperBound)
            data[1]=null;
        props.rangeSet(data[0],data[1])
    });

    const sliderStyle = {
        position: 'relative',
        width: '15vw',
        height: '5vw',
        border: '1px solid #d8d8d8',
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

    return(
        <div>
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
    )
}

export default PriceFilter
