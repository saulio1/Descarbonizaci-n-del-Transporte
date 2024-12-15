import { createBrowserRouter } from "react-router-dom";
import Root from "../../pages/Root";
import { lazy } from "react";

const ErrorPage = lazy ( ()=> import("../../pages/ErrorPage"))
const HomeContainer = lazy ( ()=> import("../../modules//home/container/HomeContainer"))



export const router = createBrowserRouter([{
    path: "/",
    element: <Root/>,
    errorElement: <ErrorPage/>,
    children: [{
        index:true,
        element: <HomeContainer/>
}]
}])