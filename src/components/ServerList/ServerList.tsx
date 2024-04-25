import Icon from "@mdi/react";
import {mdiClipboardTextMultiple} from "@mdi/js";
import {useState} from "react";
import {SERVERS, setServers} from "../../App.tsx";
import {Versions} from "../Versions/Versions.tsx";
import {getServersFiltered} from "../../Utils.tsx";

function ServerView ({ data }) {
	const onAddressClicked = () => {
		navigator.clipboard.writeText(`${data.address}:${data.port}`);
	}
	return (
		<div className="serverView" style={{
			display: "flex",
			flexFlow: "column",
			color: "white",
			backgroundColor: "#334",
			padding: 8,
			borderRadius: 8,
			minWidth: 400,
			maxWidth: 400
		}}>
			<h2>{data.name}</h2>
			<div>{data.version}</div>
			<div
				onClick={onAddressClicked}
				style={{
					display: "flex",
					justifyContent: "center",
					padding: 8,
					fontFamily: "monospace",
					backgroundColor: "#444",
					borderRadius: 3,
					cursor: "pointer",

				}}
			><div
				style={{
					flexGrow: 1
				}}>{data.address}:{data.port}</div>
				<Icon
					path={mdiClipboardTextMultiple}
					size={1}
					style={{

					}}/></div>
			<b>Leírás</b>
			<div>{data.description}</div>
		</div>
	)
}

export function ServerList() {
	const [, setServerList] = useState(new Array<ServerData>());
	const onVersionSelectionChange = (array : Array<string>) => {
		let filter : ServerFilters = {
			versions: array,
			partOfName: "",
			maxPlayersMax: 0
		};
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
