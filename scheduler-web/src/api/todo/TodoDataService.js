import axios from 'axios'
import { TASK_API_URL } from '../../Constants'

class TodoDataService {

    retrieveLatest() {
        return axios.get(`${TASK_API_URL}/latest`);
    }

    getTodo(id) {
        //console.log('executed service')
        return axios.get(`${TASK_API_URL}/${id}`);
    }


    searchTodo(pageRequest) {
        //console.log('executed service')
        return axios.post(`${TASK_API_URL}/search`, pageRequest, { headers: {
                'Content-Type': 'application/json'
            }});
    }

    deleteTodo(id) {
        //console.log('executed service')
        return axios.delete(`${TASK_API_URL}/${id}`);
    }

    patchTodo(id, patch) {
        //console.log('executed service')
        return axios.patch(`${TASK_API_URL}/${id}`, patch, { headers: {
                'Content-Type': 'application/json-patch+json'
            }});
    }

}

export default new TodoDataService()