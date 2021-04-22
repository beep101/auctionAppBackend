import {post} from './apiConsumer'

export function addItem(item,token,handler){
    post("items",item,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}