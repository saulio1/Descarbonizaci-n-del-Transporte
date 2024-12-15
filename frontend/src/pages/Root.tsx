import { Suspense } from "react"
import { Outlet } from "react-router-dom"
import Loading from "../core/components/loading/Loading"

const Root: React.FC = () => {
    return (
        <Suspense fallback = {<Loading/>}> 
            <Outlet/>
        </Suspense>
    )
}
export default Root