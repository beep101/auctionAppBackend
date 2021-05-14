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

export function getClientTokens(token,handler){
    get('getClientToken',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}