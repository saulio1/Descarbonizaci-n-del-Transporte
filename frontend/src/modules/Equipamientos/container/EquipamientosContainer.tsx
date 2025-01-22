import { Box, Button, /*Typography*/ } from "@mui/material"
import Example from "../components/ExampleEquo"
import { useNavigate } from "react-router-dom"

const EquipamientosContainer: React.FC = () => {
const navigate = useNavigate()
          return <Box display='flex' flexDirection='column'>
              
                   {/* <Typography variant="h1" fontSize='2rem'> Modulo de Equipamientos</Typography> */}
                   <Example/>
                   <Button  onClick={()=>{navigate('nuevoequip')} }sx={{alignSelf: "flex-end", color: "white", background: "blue"}}>Crear nuevo equipamiento</Button>
                    
                </Box>

        }
export default EquipamientosContainer

