import { Suspense } from "react"
import { Outlet } from "react-router-dom"
import Loading from "../core/components/loading/Loading"
import NavBar from "./components/NavBar"

const Root: React.FC = () => {
    return (
        <Suspense fallback = {<Loading/>}> 
            <NavBar/>
            <Outlet/>
        </Suspense>
    )
}
export default Root