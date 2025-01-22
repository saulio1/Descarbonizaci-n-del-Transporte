import { SxProps } from '@mui/material'

export type ColumnDefinition<T> = {
  name: keyof T
  label: string | JSX.Element
  align?: 'center' | 'inherit' | 'left' | 'right' | 'justify'
  width?: string
  sxProps?: SxProps
  headerSxProps?: SxProps
}

export type Row = Record<string, string | number> & {
  id: string | number
}

export type Pagination = {
  total: number
  page: number
  size: number
}

export type PaginationProp = Pagination & {
  onPageChange: (newPage: number) => void
}

export type Action = {
  name: string
  hide?: boolean
  onClick: (item: Row) => void
  icon: JSX.Element
}
