import { createBrowserRouter } from "react-router-dom";
import Root from "../../pages/Root";
import { lazy } from "react";


const ErrorPage = lazy ( ()=> import("../../pages/ErrorPage"))
const HomeContainer = lazy ( ()=> import("../../modules//home/container/HomeContainer"))
const DetallesTrabajadorContainer = lazy( ()=> import("../../modules//Trabajadores/modules/moduleDetallestrabajador/container/DetallesTrabajadorContainer"))
const NuevoTrabajador= lazy( ()=> import("../../modules//Trabajadores/modules/modulesNuevoTrabajador/container/NuevoTrabajador"))
const TrabajadorContainer= lazy( ()=> import("../../modules//Trabajadores/container/TrabajadorContainer"))
const TrabajadorContainerRoot= lazy( ()=> import("../../modules//Trabajadores/container/TrabajadorContainerRoot"))
const EquipamientosContainer= lazy( ()=> import("../../modules/Equipamientos/container/EquipamientosContainer"))
const EquipamientosContainerRoot= lazy( ()=> import("../../modules/Equipamientos/container/EquipamientosContainerRoot"))
const NuevoEquipamiento= lazy( ()=> import("../../modules/Equipamientos/modules/moduleNuevoEquipamiento/container/NuevoEquipamiento"))
const DetallesEquipamientoContainer= lazy( ()=> import("../../modules/Equipamientos/modules/moduleDetallesEquipamiento/container/DetallesEquipamientoContainer"))
const UsuariosContainer= lazy( ()=> import("../../modules/Usuarios/container/UsuariosContainer"))
const UsuariosContainerRoot= lazy( ()=> import("../../modules/Usuarios/container/UsuariosContainerRoot"))
const NuevoUsuarioContainer= lazy( ()=> import("../../modules/Usuarios/modules/moduleNuevoUsuario/container/NuevoUsuarioContainer"))
const DetallesUsuarioContainer= lazy( ()=> import("../../modules/Usuarios/modules/moduleDetallesUsuario/container/DetallesUsuarioContainer"))
const RolesContainerRoot=lazy( ()=> import("../../modules/Roles/container/RolesContainer"))
const RolesContainer=lazy( ()=> import("../../modules/Roles/container/RolesContainer"))



export const router = createBrowserRouter([{
    path: "/",
    element: <Root/>,
    errorElement: <ErrorPage/>,
    children: [{
        index:true,
        element: <HomeContainer/>
},
{
    path: "trabajadores", 
    element: <TrabajadorContainerRoot/>,
    children: [
        {
            index:true,
            element:<TrabajadorContainer/>,
        },
        {
            path:"nuevotrabajador",
            element:<NuevoTrabajador/>,
        },
        {
            path: "editar",
            element:<DetallesTrabajadorContainer/>,
        }
    ]
},
{
    path: "equipamiento", 
    element: <EquipamientosContainerRoot/>,
    children: [
        {
            index:true,
            element:<EquipamientosContainer/>,
        },
        {
            path:"nuevoequip",
            element:<NuevoEquipamiento/>,
        },
        {
            path: "editar",
            element:<DetallesEquipamientoContainer/>,
        }
    ]
},
{
    path: "usuarios", 
    element: <UsuariosContainerRoot/>,
    children: [
        {
            index:true,
            element:<UsuariosContainer/>,
        },
        {
            path:"nuevousuario",
            element:<NuevoUsuarioContainer/>,
        },
        {
            path: "editar",
            element:<DetallesUsuarioContainer/>,
        },
        {
            path: "roles",
            element:<RolesContainerRoot/>,
            children: [
                {
                index: true,
                element:<RolesContainer/>,
            }

            ]
        }
    ]
}]
}])