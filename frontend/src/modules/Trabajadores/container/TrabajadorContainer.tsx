import { Box, Button, Typography } from "@mui/material"
import TrabajadorTabla from "../components/tablatrabajador"
import { useNavigate } from "react-router-dom"

const TrabajadorContainer: React.FC = () => {
const navigate = useNavigate()
          return <Box display='flex' flexDirection='column'>
                
                    <Typography variant="h1" fontSize='2rem'> Modulo de trabajadores</Typography>
                    <Button  onClick={()=>{navigate('nuevotrabajador')} }sx={{alignSelf: "flex-end", color: "white"}}>Crear nuevo trabajador</Button>
                    <TrabajadorTabla/>
                </Box>

        }
export default TrabajadorContainer 