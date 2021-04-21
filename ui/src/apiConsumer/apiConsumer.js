import axios from 'axios';

const apiClient = axios.create({
    baseURL: "/api/"
});

  const { get, post, put, delete:del } = apiClient;
  export { get, post, put, del };