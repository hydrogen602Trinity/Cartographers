import { styled } from '@mui/material/styles';

import InputBase from '@mui/material/InputBase';
import SearchIcon from '@mui/icons-material/Search';
import "./SearchBar.scss";
import { Box, TextField } from '@mui/material';

// const SearchIconWrapper = styled('div')(({ theme }) => ({
//     padding: theme.spacing(0, 2),
//     height: '100%',
//     position: 'absolute',
//     pointerEvents: 'none',
//     display: 'flex',
//     alignItems: 'center',
//     justifyContent: 'center',
// }));

// const StyledInputBase = styled(InputBase)(({ theme }) => ({
//     color: 'inherit',
//     '& .MuiInputBase-input': {
//         padding: theme.spacing(1, 1, 1, 0),
//         // vertical padding + font size from searchIcon
//         paddingLeft: `calc(1em + ${theme.spacing(4)})`,
//         transition: theme.transitions.create('width'),
//         width: '100%',
//         [theme.breakpoints.up('md')]: {
//             width: '20ch',
//         },
//     },
// }));

export default function SearchBar() {
    return (
        // <div>
        //     <SearchIconWrapper>
        //         <SearchIcon />
        //     </SearchIconWrapper>
        //     <StyledInputBase
        //         placeholder="Search…"
        //         inputProps={{ 'aria-label': 'search' }}
        //     />
        // </div>
        <div className='search-bar'>
            <Box sx={{ display: 'flex', alignItems: 'flex-end', marginTop: '-16px' }}>
                <SearchIcon sx={{ color: 'action.active', mr: 1, my: 0.5 }} />
                <TextField id="input-with-sx" label="With sx" variant="standard" />
            </Box>
        </div>
    );
}