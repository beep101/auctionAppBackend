import { get, defaultErrorHandler } from './apiConsumer'

export function getPayPalOnboardUrl(token,handler){
    get('getOnboardingUrl',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}