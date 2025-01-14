import { useForm } from "react-hook-form";

const { register, handleSubmit, reset, control, setValue } = useForm();
<input type="text" ref={register} name="firstName" />

const NuevoTrabajador: React.FC = () => {
    return (
        
)
}
export default NuevoTrabajador 

// const NuevoTrabajador: React.FC = () => {
//     const { register, handleSubmit, reset, control, setValue } = useForm();
  
//     // Función para manejar el envío del formulario
//     const onSubmit = (data: any) => {
//       console.log(data);
//     };
  
//     return (
//       <form onSubmit={handleSubmit(onSubmit)}>
//         <input
//           type="text"
//           {...register("firstName")} // Ahora se usa spread operator para registrar el campo
//           placeholder="First Name"
//         />
//         <button type="submit">Submit</button>
//       </form>
//     );
//   };
  