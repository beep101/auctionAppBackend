import { post, put } from './apiConsumer'

export function modUserData(data,token,handler){
    put("account",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );

}

export function addUserAddress(data,token,handler){
    post("account/address",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );

}

export function modUserAddress(data,token,handler){
    put("account/address",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );

}

export function addUserPayMethod(data,token,handler){
    post("account/payMethod",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );

}

export function modUserPayMethod(data,token,handler){
    put("account/payMethod",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );

}

export function addUserImage(data,token,handler){
    post("account/image",data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            handler(false,null)
        }
    );
}