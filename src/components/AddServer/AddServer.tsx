import {ChangeEvent, useState} from "react";
import {VERSION_NAMES} from "../../App.tsx";
import {post, ServerData} from "../../Utils.tsx";
import './AddServer.css';

interface VersionSelectProprs {
	onChange: (event : ChangeEvent<HTMLSelectElement>) => void,
	defaultOption: number
}

export function VersionSelect({ onChange, defaultOption } : VersionSelectProprs) {
	return (
		<select
			name="version"
			defaultValue={VERSION_NAMES.length == 0 ? "" : VERSION_NAMES[defaultOption]}
			onChange={onChange}>
			{VERSION_NAMES.map(k => (
				<option key={k} value={k}>{k}</option>
			))}
		</select>
	)
}

interface AddServerProps {
	onSuccess: () => void
}
export function AddServer({ onSuccess } : AddServerProps) {
	const [data, setData] = useState<ServerData>({
		version: 'null',
		name: '',
		description: '',
		address: '',
		port: 25565,
		maxPlayers: 20
	});
	const [msg, setMsg] = useState({
		msg: "",
		color: "transparent",
		list: null
	});

	const handleInputChange = (event) => {
		const { name, value } = event.target;
//		console.log(`new server data change: ${name} -> ${value}`);
		setData(prevData => ({
			...prevData,
			[name]: name === 'port' || name === 'maxPlayers' ? parseInt(value) : value
		}));
		console.log("data -> " + JSON.stringify(data));
	};

	if (VERSION_NAMES.length > 0 && data.version === 'null') {
		setData(prevData => ({
			...prevData,
			version: VERSION_NAMES[0]
		}));
	}

	const handleSubmit = (event : MouseEvent) => {
		event.preventDefault();
		console.log("new server data: " + JSON.stringify(data));
		post("/servers", data)
			.then(async resp => {
				let text = "❌ Nem sikerült hozzáadni a szervert 😾";
				let color = "rgba(160, 64, 64, 1)";
				let list = null;
				if (resp.status === 200) {
					text = "✅ Szerver hozzáadva 😎";
					color = "rgba(64, 160, 64, 0.5)";
					onSuccess();
				}
				else if (resp.status == 400) {
					let json = await resp.json();
					let v = Object.values(json)
						.map(x => (<li>{x}</li>));
					list = (<ul style={{
						backgroundColor: color
					}}>{v}</ul>);
				}
				else if (resp.status === 409) {
					text = `❌ Ezen a címen (${data.address}) már található regisztrált szerver 😾`;
				}
				else {
					text = await resp.text();
//					text += ` (kód: ${resp.status}`;
				}
				setMsg({
					msg: text,
					color: color,
					list: list
				});
			});
	};
	return (
		<div>
			<form>
				<table>
					<tbody>
					<tr>
						<td><label>Név:</label></td>
						<td><input
							name="name"
							type="text"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td>Max játékosszám:<label></label></td>
						<td><input
							name="maxPlayers"
							type="number"
							defaultValue="20"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td><label>Verzió:</label></td>
						<td><VersionSelect onChange={handleInputChange} defaultOption={0} /></td>
					</tr>
					<tr>
						<td><label>Elérés:</label></td>
						<td style={{
							display: "flex",
						}}>
							<input
								name="address"
								type="text"
								placeholder="IP-cím vagy domain"
								onChange={handleInputChange}
								className="flexGrow1"/>
							:<input
								name="port"
								type="number"
								style={{
									width: "5em"
								}}
								defaultValue="25565"
								placeholder="port"
								onChange={handleInputChange}/></td>
					</tr>
					</tbody>
				</table>
				<b>Leírás:</b>
				<textarea
					name="description"
					rows={8}
					onChange={handleInputChange}>
				</textarea>
				<button
					className="buttonSubmit"
					onClick={handleSubmit}>Hozzáadás</button>
				<div style={{
					backgroundColor: msg.color,
					textAlign: "center",
					fontSize: 16,
					padding: 4,
				}}>{msg.msg}</div>
				{msg.list !== null && msg.list}
			</form>
		</div>
	)
}
