export const validateEmail=(email)=> {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

export function timeDiffTodayToDateString(date){
    if(typeof date!='string')
        return null
    let dateComponentsArr = date.split(/[-T:.]/);
    let endtime = new Date(Date.UTC(dateComponentsArr[0], dateComponentsArr[1]-1, dateComponentsArr[2], dateComponentsArr[3], dateComponentsArr[4], dateComponentsArr[5]));
    let secs=(endtime.getTime()-Date.now())/1000;

    if(secs/60>1){
        let mins=secs/60;
        if(mins/60>1){
            let hrs=mins/60;
            if(hrs/24>1){
                return Math.round(hrs/24)+' days';
            }else{
                return Math.round(hrs)+' hours';
            }
        }else{
            return Math.round(mins/60)+' minutes';
        }
    }else{
        return Math.round(secs/60)+' seconds';
    }
}