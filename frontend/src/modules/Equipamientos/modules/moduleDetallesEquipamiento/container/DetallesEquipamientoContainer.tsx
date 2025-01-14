import { useParams } from "react-router-dom"

const DetallesEquipamientoContainer: React.FC = () => {
    const {id} = useParams()
    return (
        <div>Equip con el id: {id}</div>
)
}
export default DetallesEquipamientoContainer 