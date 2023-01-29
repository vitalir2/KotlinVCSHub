import {Avatar, Box, Stack, Typography} from "@mui/material";
import React from "react";
import {User} from "../login/AuthRepository";

export interface ProfileViewProps {
    user?: User,
}

export function Profile(props: ProfileViewProps) {
    const { user } = props

    if (user == null) {
        // TODO make better placeholder
        return (
            <Box>
                <Typography variant={"h3"}>
                    User is loading
                </Typography>
            </Box>
        )
    }

    return (
        <Stack spacing={1} sx={{
            margin: 2,
        }}>
            <Avatar alt={"Account picture"} src={"#"} sx={{
                width: 75,
                height: 75,
                padding: 1,
            }}/>
            <Typography variant={"h5"}>
                {user.username}
            </Typography>
            <Typography variant={"body1"}>
                Description
            </Typography>
        </Stack>
    )
}
