import { Box, IconButton, Typography } from '@mui/material'
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos'
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'

interface Props {
  total: number
  page: number
  size: number
  onPageChange: (newPage: number) => void
}

const TablePagination: React.FC<Props> = ({
  total,
  page,
  size,
  onPageChange
}) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexFlow: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        py: 1
      }}
    >
      <IconButton
        aria-label="prev-page"
        onClick={() => onPageChange(page - 1)}
        disabled={page === 1}
      >
        <ArrowBackIosIcon sx={{ color: '#424242', height: '20px' }} />
      </IconButton>
      <Typography variant="body2" color="#424242">
        Page {page}
      </Typography>
      <IconButton
        aria-label="next-page"
        onClick={() => onPageChange(page + 1)}
        disabled={page * size >= total}
      >
        <ArrowForwardIosIcon sx={{ color: '#424242', height: '20px' }} />
      </IconButton>
    </Box>
  )
}

export default TablePagination
