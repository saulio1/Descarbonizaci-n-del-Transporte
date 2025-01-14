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
            path: ":id",
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
            path: ":id",
            element:<DetallesEquipamientoContainer/>,
        }
    ]
}]
}])