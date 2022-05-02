import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import Divider from '@mui/material/Divider';
import FormControl from '@mui/material/FormControl';
import IconButton from '@mui/material/IconButton';
import InputBase from '@mui/material/InputBase';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';

import "./SearchBar.scss";

/**
 * Properties used by SearchBar
 */
export interface ISearchProps {
    onSearch: (_: string) => void,
    defaultValue: string
}


export default function SearchBar({ onSearch, defaultValue }: ISearchProps) {
    const [searchTerm, setSearchTerm] = useState(defaultValue);


    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        onSearch(searchTerm);
    }

    return (
        <Paper sx={{ p: '2px 4px', display: 'flex', alignItems: 'center' }}>
            <IconButton sx={{ p: '10px' }} aria-label="search" onClick={() => onSearch(searchTerm)}>
                <SearchIcon />
            </IconButton>
            <FormControl component="form" fullWidth onSubmit={handleSubmit}>
                <InputBase
                    onChange={e => setSearchTerm(e.target.value)}
                    value={searchTerm}
                    sx={{ ml: 1, flex: 1 }}
                    className='input-base'
                    placeholder="Search"
                    inputProps={{ 'aria-label': 'search' }}
                />
            </FormControl >
            <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
            <IconButton sx={{ p: '10px' }} aria-label="menu">
                <MenuIcon />
            </IconButton>
        </Paper>
    );
}
