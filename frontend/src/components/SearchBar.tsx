import SearchIcon from '@mui/icons-material/Search';
import InputBase from '@mui/material/InputBase';
import { useState } from 'react';

import "./SearchBar.scss";

/**
 * Properties used by SearchBar
 */
export interface ISearchProps {
    callback: (_: string) => void,
    defaultValue: string
}


export default function SearchBar({ callback, defaultValue }: ISearchProps) {
    const [searchTerm, setSearchTerm] = useState(defaultValue);


    const submitHandler = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        callback(searchTerm);
    }

    return (
        <div className='search-bar'>
            <div className='icon-wrapper'>
                <SearchIcon />
            </div>
            <form onSubmit={submitHandler}>
                <InputBase
                    onChange={e => setSearchTerm(e.target.value)}
                    value={searchTerm}
                    className='input-base'
                    placeholder="Search…"
                    inputProps={{ 'aria-label': 'search' }}
                />
            </form>
        </div>
    );
}
