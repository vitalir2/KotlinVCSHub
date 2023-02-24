import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box, CircularProgress, Stack, SxProps, Tab, Tabs, Typography} from "@mui/material";
import {ReactElement, useEffect, useMemo, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {fetchRepository} from "./RepositorySlice";
import {User} from "../../user/User";
import {fetchCurrentUser} from "../../user/UserSlice";
import {log} from "util";

export function RepositoryPage() {
    const dispatch = useAppDispatch()

    const { repositoryId } = useParams()
    const user = useAppSelector(state => state.user.user)
    const repository = useAppSelector(state => state.repository.repository)

    useEffect(() => {
        if (repositoryId === undefined) return
        dispatch(fetchRepository(repositoryId))
        dispatch(fetchCurrentUser())
    }, [repositoryId, dispatch])

    if (repository.kind === "loading" || user.kind === "loading") {
        return <LoadingPlaceholder/>
    }

    if (repositoryId === undefined || repository.kind === "error" || user.kind === "error") {
        return <ErrorPlaceholder error={"Something went wrong"} />
    }

    return <RepositoryContainer user={user.data} repository={repository.data}/>
}

function LoadingPlaceholder() {
    return (
        <Box sx={{
            width: "100%",
            height: "100vh", // TODO fix to be 100% of free space
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <CircularProgress/>
        </Box>
    )
}

interface ErrorPlaceholderProps {
    error: string,
}

function ErrorPlaceholder(props: ErrorPlaceholderProps) {
    return (
        <Box sx={{
            width: "100%",
            height: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <Typography variant={"h6"}>
                {props.error}
            </Typography>
        </Box>
    )
}

interface RepositoryContainerProps {
    user: User,
    repository: Repository,
}

const repositoryPageTabStyle: SxProps = {
    marginRight: "20vw",
}

function RepositoryContainer(props: RepositoryContainerProps) {
    const [currentTab, setCurrentTab] = useState(0)
    const { user, repository } = props
    let tabPanel: ReactElement = useMemo(() => {
        switch (currentTab) {
            case 0:
                return <RepositoryInfo/>
            case 1:
                return <RepositorySettingsInfo />
            default:
                console.log(`Unknown repository page tab=${currentTab}`)
                return <RepositoryInfo/>
        }
    }, [currentTab])

    return (
        <Stack spacing={2} sx={{
            padding: "1rem",
        }}>
            <Typography variant={"h5"}>
                {`${user.username} / ${repository.name}`}
            </Typography>
            <Tabs
                value={currentTab}
                onChange={(e, value: number) => setCurrentTab(value)}
                sx={{
                    width: "90vw",
                }}
            >
                <Tab label={"Code"} sx={repositoryPageTabStyle}/>
                <Tab label={"Settings"} sx={repositoryPageTabStyle}/>
            </Tabs>
            {tabPanel}
        </Stack>
    )
}

// TODO
function RepositoryInfo() {
    return (
        <Stack>
            Repo Info
        </Stack>
    )
}

// TODO
function RepositorySettingsInfo() {
    return (
        <Stack>
            Settings Info
        </Stack>
    )
}
