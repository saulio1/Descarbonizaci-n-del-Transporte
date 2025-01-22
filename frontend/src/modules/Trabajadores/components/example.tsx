import React, { useMemo } from 'react'
import Table from './TablaTrabajadores'
import { Action, ColumnDefinition, Pagination, Row } from './types'
import { Delete, Edit } from '@mui/icons-material'
import { useNavigate } from "react-router-dom"

//Define your columns here

const definition: ColumnDefinition<Row>[] = [
  {
    name: 'name',
    label: 'Nombre y Apellidos',
    align: 'center',
    width: '240px',
    sxProps: { backgroundColor: 'pink' },
    headerSxProps: { backgroundColor: 'salmon', color: 'white' }
  },
  {
    name: 'ci',
    label: 'Carnet de identidad',
    align: 'right',
    width: '120px',
    sxProps: null,
    headerSxProps: null
  },
  {
    name: 'numero',
    label: 'Número de teléfono',
    align: 'right',
    width: '120px',
    sxProps: null,
    headerSxProps: null
  },
  {
    name: 'correo',
    label: <>Correo Electrónico</>,
    align: 'right',
    width: '120px',
    sxProps: null,
    headerSxProps: null
  },
  {
    name: 'ocupacion',
    label: <>Ocupación</>,
    align: 'right',
    width: '120px',
    sxProps: null,
    headerSxProps: null
  },
  {
    name: 'estado',
    label: <>Estado</>,
    align: 'right',
    width: '120px',
    sxProps: null,
    headerSxProps: null
  }
]

//Function to generate data, only valid for this example,
// the data must to be fetched from backend and cleaned before use
function createData(
  id: number,
  name: string,
  ci: string,
  numero: string,
  correo: string,
  ocupacion: string,
  estado: string,
) {
  return { id, name, ci, numero, correo, ocupacion,estado }
}

//All your data
const allRows = [
  createData(1, 'Saulio', "02081960207", "53976500", "saulio@icloud.com", "Chofer", "Activo" ),
//   createData(2, 'Ice cream sandwich', 237, 9.0, 37, 4.3, "Activo"),
//   createData(3, 'Eclair', 262, 16.0, 24, 6.0, "Activo"),
//   createData(4, 'Cupcake', 305, 3.7, 67, 4.3,"Activo"),
//   createData(5, 'Frozen yoghurt', 159, 6.0, 24, 4.0,"Activo"),
//   createData(6, 'Ice cream sandwich', 237, 9.0, 37, 4.3,"Activo"),
//   createData(7, 'Eclair', 262, 16.0, 24, 6.0,"Activo"),
//   createData(8, 'Cupcake', 305, 3.7, 67, 4.3,"Activo"),
//   createData(9, 'Frozen yoghurt', 159, 6.0, 24, 4.0,"Activo"),
//   createData(10, 'Ice cream sandwich', 237, 9.0, 37, 4.3,"Activo"),
//   createData(11, 'Frozen yoghurt', 159, 6.0, 24, 4.0),
//   createData(12, 'Ice cream sandwich', 237, 9.0, 37, 4.3,"Activo"),
//   createData(13, 'Eclair', 262, 16.0, 24, 6.0,"Activo"),
//   createData(14, 'Eclair', 262, 16.0, 24, 6.0,"Activo"),
//   createData(15, 'Cupcake', 305, 3.7, 67, 4.3,"Activo"),
]



const Example = () => {
  //Pagination Object
  const [pagination, setPagination] = React.useState<Pagination>({
    page: 1,
    size: 10,
    total: allRows.length
  })

  const onPageChange = (page: number) => {
    setPagination((prev) => ({ ...prev, page }))
  }

  //Data Pagination
  const rows = useMemo<Row[]>(
    () =>
      allRows.slice(
        (pagination.page - 1) * pagination.size,
        Math.min(pagination.page * pagination.size)
      ),
    [pagination.page, pagination.size]
  )

  //Send this object only when the data have to be paginated
  const paginationProp = { ...pagination, onPageChange }

  // Define Actions
  const navigate = useNavigate()
  const actions: Action[] = [
    {
      name: 'edit',
      onClick: (/*item: Row*/) => {
        {navigate('editar')}
        //To Edit Element in another view
       // window.location.href = `edit/${item.id}`
      },
      icon: <Edit />
    },
    {
      name: 'delete',
      onClick: (item: Row) => {
        //Delete data
        console.log('Delete item: ', item.id) //After Delete you have to update current data
      },
      icon: <Delete />
    }
  ]
  
  return (
    <>
    
      <h1 style={{ textAlign: 'center', color:"blue" }}>Módulo de Trabajadores</h1>
      <br />
      <Table
        columnDefinition={definition}
        rows={rows}
        pagination={paginationProp}
        actions={actions}
        sxAction={{ backgroundColor: '#e2e2e2' }}
      />
    </>
  )
}

export default Example
