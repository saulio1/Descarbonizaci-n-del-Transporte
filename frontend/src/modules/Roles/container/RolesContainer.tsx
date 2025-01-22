import { Box, Button, /*Typography*/ } from "@mui/material"
import { useNavigate } from "react-router-dom"
import Example from "../components/example"

const RolesContainer: React.FC = () => {
const navigate = useNavigate()
          return <Box display='flex' flexDirection='column'>
                
                   {/* // <Typography variant="h1" fontSize='2rem'> Módulo de trabajadores</Typography> */}
                    <Example/>
                    <Button  onClick={()=>{navigate('nuevorol')} }sx={{alignSelf: "flex-end", color: "white", background: "blue", margin: "0.2%"}}>Crear nuevo Rol</Button>
                </Box>

        }
export default RolesContainer 