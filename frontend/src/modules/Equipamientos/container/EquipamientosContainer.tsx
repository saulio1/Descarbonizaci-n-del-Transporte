import { Box, Button, Typography } from "@mui/material"
import EquipamientoTabla from "../components/TablaEquipamientos"
import { useNavigate } from "react-router-dom"

const EquipamientosContainer: React.FC = () => {
const navigate = useNavigate()
          return <Box sx={{backgroundColor: "red",}} display='flex' flexDirection='column'>
              
                   <Typography variant="h1" fontSize='2rem'> Modulo de Equipamientos</Typography>
                   <Button  onClick={()=>{navigate('nuevoequip')} }sx={{alignSelf: "flex-end", color: "white"}}>Crear nuevo equipamiento</Button>
                    <EquipamientoTabla/>
                </Box>

        }
export default EquipamientosContainer