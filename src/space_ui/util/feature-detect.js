
export function isDateSupported() {
	var input = document.createElement('input');
	var value = 'a';
	input.setAttribute('type', 'date');
	input.setAttribute('value', value);
	return (input.value !== value);
};

export function isTimeSupported() {
	var input = document.createElement('input');
	var value = 'a';
	input.setAttribute('type', 'time');
	input.setAttribute('value', value);
	return (input.value !== value);
};
