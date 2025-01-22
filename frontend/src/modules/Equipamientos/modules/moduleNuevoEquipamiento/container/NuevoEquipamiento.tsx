import { useForm } from "react-hook-form";
import { FormControl } from "@mui/material";
import {TextField} from "@mui/material";
import { Controller } from "react-hook-form";
import MenuItem from "@mui/material/MenuItem";
// import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
// import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
// import { DatePicker } from "@mui/x-date-pickers/DatePicker";



const NuevoEquipamiento: React.FC = () => {
    const { /*register,reset,setValue,*/ handleSubmit,control } = useForm();
  
    // Función para manejar el envío del formulario
    const onSubmit = (data: unknown) => {
      console.log(data);
    };
    return (
         <form onSubmit={handleSubmit(onSubmit)}>
         <Controller
        name="nombre"
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
               label="Nombre"              // Etiqueta visible para el usuario
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
        name="Categoría"
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
              label="Categoría"
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
        name="ubicacion"
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
              label="Ubicación"
              required
              id="ubicacion"
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
        name="select"
        control={control}
        render={({
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
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
            

              
            >
             <>
<MenuItem value="Activo"> Activo </MenuItem>
<MenuItem value="Licencia"> Licencia </MenuItem>
<MenuItem value="Despedido"> Despedido </MenuItem>

</>
           </TextField>
          </FormControl>
        )}
/>
{/* <LocalizationProvider dateAdapter={AdapterDateFns}>
      <Controller
        name={"date"}
        control={control}
        render={({ field: { onChange, value } }) => (
          <DatePicker value={value} onChange={onChange} />
        )}
      />
    </LocalizationProvider>
       */}
        <button type="submit">Guardar</button>
      </form>
    );
}
//<FormInputDate name="dateValue" control={control} label="Date" />



export default NuevoEquipamiento 


