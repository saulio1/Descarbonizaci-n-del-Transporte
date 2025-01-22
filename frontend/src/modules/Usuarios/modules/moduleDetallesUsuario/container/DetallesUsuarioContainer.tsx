// import { useParams } from "react-router-dom"

// const DetallesUsuarioContainer: React.FC = () => {
//     const {id} = useParams()
//     return (
        
// )
// }
// export default DetallesUsuarioContainer 
import { useForm } from "react-hook-form";
import { FormControl } from "@mui/material";
import {TextField} from "@mui/material";
import { Controller } from "react-hook-form";
import MenuItem from "@mui/material/MenuItem";

const DetalesUsuarioContainer: React.FC = () => {
    const { /*register,reset,, setValue*/ handleSubmit,  control } = useForm();
  
    // Función para manejar el envío del formulario
    const onSubmit = (data: unknown) => {
      console.log(data);
    };
    return (
         <form onSubmit={handleSubmit(onSubmit)}>
         <Controller
        name="username"
        control={control}
        render={({
          field: { value, onChange, onBlur, ref },
          fieldState: { error }
        }) => (
          <FormControl
            required
            fullWidth
            variant="standard"
            error={Boolean(error)}
          >
            <TextField
               label="Nombre de Usuario"              // Etiqueta visible para el usuario
               required                        // Hace obligatorio el campo
               id="username"                 // Identificador único del campo
               variant="outlined"              // Usa el diseño de borde "outlined"
               value={value ?? ''}             // Controla el valor actual del campo
               onChange={onChange}             // Maneja cambios en el valor del campo
               onBlur={onBlur}                 // Maneja el evento de "perder el foco"
               inputRef={ref}                  // Conecta el campo con React Hook Form
               fullWidth                       // Asegura que ocupe todo el ancho disponible
            />
          </FormControl>
        )}
/>
<Controller
        name="password"
        control={control}
        render={({
          field: { value, onChange, onBlur, ref },
          fieldState: { error }
        }) => (
          <FormControl
            required
            fullWidth
            variant="standard"
            error={Boolean(error)}
          >
            <TextField
              label="Contraseña"
              required
              id="password"
              variant="outlined"
              value={value ?? ''}
              onChange={onChange}
              onBlur={onBlur}
              inputRef={ref}
              fullWidth    
              //sx={{ width: '500px' }}
            />
          </FormControl>
        )}
/>

<Controller
        name="Correo Electrónico"
        control={control}
        render={({
          field: { value, onChange, onBlur, ref },
          fieldState: { error }
        }) => (
          <FormControl
            required
            fullWidth
            variant="standard"
            error={Boolean(error)}
          >
            <TextField
              label="Correo Electrónico"
              required
              id="password"
              variant="outlined"
              value={value ?? ''}
              onChange={onChange}
              onBlur={onBlur}
              inputRef={ref}
              fullWidth    
              //sx={{ width: '500px' }}
            />
          </FormControl>
        )}
/>
<Controller
        name="rol"
        control={control}
        render={({
          field: { value, onChange, onBlur },
          fieldState: { error }
        }) => (
          <FormControl
            required
            fullWidth
            variant="standard"
            error={Boolean(error)}
          >
           <TextField
              id="id"
              name="name"
              select
              // native="true"
              // className={classes.textField}
              label="Rol"
              margin="normal"
              variant="outlined"
              onChange={onChange}
              onBlur={onBlur} 
              value={value}

              
            >
              <MenuItem value=""/>            
              <MenuItem value="Admin"> Admin </MenuItem>
              <MenuItem value="Trabajador"> Trabajador </MenuItem>
              <MenuItem value="Jefe"> Jefe </MenuItem>
           </TextField>
          </FormControl>
        )}
/>


      
        <button type="submit">Submit</button>
      </form>
    );
}
  export default DetalesUsuarioContainer