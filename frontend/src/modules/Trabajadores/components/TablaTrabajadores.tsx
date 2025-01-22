import MUITable from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Paper from '@mui/material/Paper'
import { Action, ColumnDefinition, PaginationProp, Row } from './types'
import { FC } from 'react'
import TablePagination from './TablaPaginacion'
import { IconButton, SxProps } from '@mui/material'
import DisabledByDefaultRoundedIcon from '@mui/icons-material/DisabledByDefaultRounded'


interface Props<T> {
  rows: T[]
  columnDefinition: ColumnDefinition<T>[]
  pagination?: PaginationProp
  actions?: Action[]
  sxAction?: SxProps
}

const Table: FC<Props<Row>> = ({
  columnDefinition,
  rows,
  pagination,
  actions = [],
  sxAction
}) => {
  if (rows.length === 0)
    return (
      <>
        <h3 style={{ textAlign: 'center', margin: 0 }}>No Data Available</h3>
        <DisabledByDefaultRoundedIcon
          sx={{ color: '#e2e2e2', fontSize: '80px', alignSelf: 'center' }}
        />
      </>
    )

  return (
    <>
      <TableContainer component={Paper} sx={{ maxHeight: 430 }}>
        <MUITable sx={{ minWidth: 650 }} stickyHeader aria-label="simple table">
          <TableHead>
            <TableRow>
              {columnDefinition.map((column) => (
                <TableCell
                  key={`${String(column.name)}-header`}
                  align={column.align}
                  width={column.width}
                  sx={{ backgroundColor: '#e2e2e2', ...column.headerSxProps }}
                >
                  {column.label}
                </TableCell>
              ))}
              {actions.length > 0 && (
                <TableCell
                  key={`action-header`}
                  align="center"
                  width={`${actions.length * 40}px`}
                  sx={{
                    minWidth: '80px',
                    ...sxAction
                  }}
                >
                  Actions
                </TableCell>
              )}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row) => (
              <TableRow
                key={row.id}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                {columnDefinition.map((column) => (
                  <TableCell
                    key={String(column.name)}
                    align={column.align}
                    width={column.width}
                    sx={column.sxProps}
                  >
                    {row[column.name]}
                  </TableCell>
                ))}
                {actions.length > 0 && (
                  <TableCell
                    key={`action-header`}
                    align="center"
                    width={`${actions.length * 40}px`}
                    sx={{
                      minWidth: '80px'
                    }}
                  >
                    {actions.map(
                      (action, index) =>
                        !action.hide && (
                          <IconButton
                            key={`action-${index} ${action.name}`}
                            onClick={() => action.onClick(row)}
                          >
                            {action.icon}
                          </IconButton>
                        )
                    )}
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </MUITable>
      </TableContainer>
      {pagination && <TablePagination {...pagination} />}
    </>
  )
}

export default Table
