import Icon from "@mdi/react";
import {mdiClipboardTextMultiple} from "@mdi/js";
import {useState} from "react";
import {SERVERS, setFilters, setServers, VERSION_NAMES} from "../../App.tsx";
import {Versions} from "../Versions/Versions.tsx";
import {getServersFiltered, ServerData, ServerFilters} from "../../Utils.tsx";
import './ServerList.css';

function ServerAddress({ address, port }) {

	const onAddressClicked = (event) => {
//		navigator.clipboard.writeText(`${data.address}:${data.port}`);
		console.log("address: " + event.target.innerText);
		navigator.clipboard.writeText(event.target.innerText);
	};
	return (
		<div
			onClick={onAddressClicked}
			className="serverAddress">
			<div className="serverAddressText">{address}:{port}</div>
			<div className="iconDiv">
				<Icon
					path={mdiClipboardTextMultiple}
					size={1}/>
			</div>
		</div>
	)
}

function ServerView({ data }) {
	const [editable, setEditable] = useState(true);
	const [serverData, setServerData] = useState({
/*		name: data.name,
		version: data.version,
		address: data.address,
		port: data.port,
		description: data.description*/
	...data});
	const handleBlur = (name : string) => (event) => {
		setServerData(prevData => ({
			...prevData, [name]: event.target.innerText
		}));
		console.log("edit server data: " + serverData);
	};
	return (
		<div className="serverView">
			<h2
				suppressContentEditableWarning
				contentEditable={editable}
				onBlur={handleBlur("name")}>{data.name}</h2>
			{
				editable ?
				(<select
					name="version"
					defaultValue={data.version}
					onBlur={handleBlur("version")}>
					{VERSION_NAMES.map(k => (
						<option key={k} value={k}>{k}</option>
					))}
				</select>)
				: (<div>{data.version}</div>)}
			<ServerAddress address={data.address} port={data.port} />
			<b>Leírás</b>
			<div
				contentEditable={editable}
				suppressContentEditableWarning
			>{data.description}</div>
		</div>
	)
}

interface ServerListProps {
	updateData : () => void;
}

export function ServerList({ updateData } : ServerListProps) {
	const [, setServerList] = useState(new Array<ServerData>());
	const onVersionSelectionChange = (array : Array<string>) => {
		let filter : ServerFilters = {
			versions: array,
			partOfName: "",
			maxPlayersMax: 0
		};
		setFilters(filter);
		getServersFiltered(array => {
			setServers(array);
			setServerList(SERVERS);
		}, filter);
	}
	return (
		<div style={{
			display: "flex",
			flexFlow: "column",
			minWidth: "100%"
		}}>
			<Versions onSelectionChange={onVersionSelectionChange}/>
			<div style={{
				display: "flex",
//			flexFlow: "column",
				flexWrap: "wrap",
				width: "100%",
				gap: 8
			}}>{SERVERS.map((x, i) => (<ServerView key={i} data={x}/>))}</div>
		</div>
	)
}
