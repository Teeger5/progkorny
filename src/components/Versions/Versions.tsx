import {useState} from "react";
import {VERSIONS} from "../../App.tsx";

function Tag({  onClick, text, count, selected }) {
//	const [selected, setSelected] = useState(false);
//	console.log(`Tag: ${text} -> ${selected}`);
	return (
		<div onClick={onClick} style={{
			display: "flex",
			backgroundColor: selected == "1" ? "#4a4" : "transparent",
			borderRadius: 12,
			borderStyle: "solid",
			borderWidth: 3,
			borderColor: "#4a4",
			padding: 8,
			paddingLeft: 16,
			paddingRight: 16,
			fontSize: 22,
			fontWeight: "bold",
			cursor: "pointer"
		}}>{text}
			<div style={{
				fontWeight: "normal",
				paddingLeft: 8
			}}>({count})</div></div>
	)
}

export function Versions({ onSelectionChange }) {
	const [selectedTags, setSelectedTags] = useState(new Array<string>());
	const onTagClicked = (value: string) => {
		console.log("tag value: " + value);
		let array = selectedTags.includes(value)
			? selectedTags.filter((x) => x !== value)
			: [...selectedTags, value];
		console.log("tags array: " + JSON.stringify(array));
		setSelectedTags(array);
		onSelectionChange(array);
	}
	return (
		<>
			<h3>Verziók</h3>
			<div style={{
				display: "flex",
				gap: 8
			}}>
				{Array.from(VERSIONS)
					.filter(([, v]) => v > 0)
					.map(([k, v]) => (
						<Tag
							key={k}
							onClick={() => onTagClicked(k)}
							text={k}
							count={v}
							selected={selectedTags.includes(k) ? "1" : "0"}
						/>
					))}
			</div></>
	);
}
