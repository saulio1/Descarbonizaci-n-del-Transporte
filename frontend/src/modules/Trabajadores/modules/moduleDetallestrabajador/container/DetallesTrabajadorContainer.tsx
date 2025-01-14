import { useParams } from "react-router-dom"

const DetallesTrabajadorContainer: React.FC = () => {
    const {id} = useParams()
    return (
        <div>Trabajador con el id: {id}</div>
)
}
export default DetallesTrabajadorContainer 