import { Box, /*Button, Stack, Typography*/ } from "@mui/material"
import bimg from "../../../assets/CUBA.jpg";
const Home: React.FC = () => {
    return (
    <Box sx={{width: "100%",
        height: "100%",
        margin:"0",
        padding:"0",
        backgroundImage: `url(${bimg})`,
        backgroundRepeat: "no-repeat",
        backgroundSize:"cover"}}>
        
        
{/* <Typography variant="h1" sx={{fontSize: "2rem", color: 'blue'}}> Descarbonización del Transporte </Typography>// */}


{/* <Stack spacing={2}>
  <Button variant="text">Gestión de Trabajadores</Button>
  <Button variant="text">Gestión de Equipamientos</Button>
 
</Stack> */}
    </Box> 
    )
}
export default Home