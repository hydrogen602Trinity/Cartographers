import CloseIcon from '@mui/icons-material/Close';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import Divider from '@mui/material/Divider';
import FormControl from '@mui/material/FormControl';
import IconButton from '@mui/material/IconButton';
import InputBase from '@mui/material/InputBase';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';


/**
 * Properties used by SearchBar
 */
export interface ISearchProps {
    onSearch: (_: string) => void,
    defaultValue: string
}


export default function SearchBar({ onSearch, defaultValue }: ISearchProps) {
    const [searchQuery, setSearchQuery] = useState(defaultValue);

    const handleSearchClear = () => {
        setSearchQuery("");
        onSearch("");
    }

    const handleFormSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        onSearch(searchQuery);
    };

    return (
        <Paper sx={{ p: '2px 4px', display: 'flex', alignItems: 'center' }}>
            <IconButton sx={{ p: '10px' }} aria-label="Search" onClick={() => onSearch(searchQuery)}>
                <SearchIcon />
            </IconButton>
            <FormControl component="form" fullWidth onSubmit={handleFormSubmit}>
                <InputBase
                    onChange={e => setSearchQuery(e.target.value)}
                    value={searchQuery}
                    sx={{ ml: 1, flex: 1 }}
                    className='input-base'
                    placeholder="Search"
                    inputProps={{ 'aria-label': 'search term' }}
                />
            </FormControl >
            {searchQuery !== "" &&
                <IconButton sx={{ p: '10px' }} aria-label="Clear Search" onClick={() => handleSearchClear()}>
                    <CloseIcon />
                </IconButton>
            }
            <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
            <IconButton sx={{ p: '10px' }} aria-label="Menu">
                <MenuIcon />
            </IconButton>
        </Paper>
    );
}
