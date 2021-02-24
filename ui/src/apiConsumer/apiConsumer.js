import axios from 'axios';

const apiClient = axios.create({
    baseURL: "/api/"
});

/*
apiClient.interceptors.request.use(
    //interceptor code
);
  
apiClient.interceptors.response.use(
    //interceptor code
);
*/

  const { get, post, put, delete: del } = apiClient;
  export { get, post, put, del };