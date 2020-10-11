import axios from "axios"
import errorParser from '@/utils/error-parser'


export default {
    /**
     * Create a board
     * @param {*} detail the detail of the board
     */
    create(detail) {
        return new Promise((resolve, reject) => {
            axios.post('/boards', detail).then(({data}) => {
                resolve(data)
            }).catch((error) => {
                reject(errorParser.parse(error))
            })          
        })
    }
}