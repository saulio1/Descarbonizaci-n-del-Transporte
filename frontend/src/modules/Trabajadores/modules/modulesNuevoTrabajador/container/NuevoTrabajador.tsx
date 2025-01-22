
import { useForm } from "react-hook-form";
import { FormControl } from "@mui/material";
import {TextField} from "@mui/material";
import { Controller } from "react-hook-form";
import MenuItem from "@mui/material/MenuItem";

const NuevoTrabajador: React.FC = () => {
    const { /*register,reset,, setValue*/ handleSubmit,  control } = useForm();
  
    // Función para manejar el envío del formulario
    const onSubmit = (data: unknown) => {
      console.log(data);
    };
    return (
         <form onSubmit={handleSubmit(onSubmit)}>
         <Controller
        name="first_name"
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
               label="Nombre completo"              // Etiqueta visible para el usuario
               required                        // Hace obligatorio el campo
               id="first_name"                 // Identificador único del campo
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
        name="carnet de identidad"
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
              label="Carnet de Identidad"
              required
              id="last_name"
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
        name="Número de Teléfono"
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
              label="Número de Teléfono"
              required
              id="last_name"
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
              id="last_name"
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
        name="ocupación"
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
              label="ocupación"
              margin="normal"
              variant="outlined"
              onChange={onChange}
              onBlur={onBlur} 
              value={value}

              
            >
              <MenuItem value=""/>            
              <MenuItem value="Chofer"> Chofer </MenuItem>
              <MenuItem value="Recepcionista"> Recepcionista </MenuItem>
              <MenuItem value="Administrador"> Administrador </MenuItem>
           </TextField>
          </FormControl>
        )}
/>
<Controller
        name="select"
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
              label="Estado"
              margin="normal"
              variant="outlined"
              onChange={onChange}
              onBlur={onBlur}
              value={value}

              
            >
              <MenuItem value=""/>            
              <MenuItem value="Activo"> Activo </MenuItem>
              <MenuItem value="Licencia"> Licencia </MenuItem>
              <MenuItem value="Despedido"> Despedido </MenuItem>
           </TextField>
          </FormControl>
        )}
/>

      
        <button type="submit">Submit</button>
      </form>
    );
}
  export default NuevoTrabajador