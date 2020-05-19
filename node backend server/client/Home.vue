<template>
	<!-- VAN AND SIYI. PLEASE LOOK AT THE COMMENTS IN THIS FILE REGARDING HOW TO USE A DONUT CHART  -->
	<div class="home fadeIn">
		<div class="padding text-center">
			<h1 class="display-2">COVID 19 - DATA</h1>
			<p class="mt-4">
				Welcome To SecuLock
			</p>
			<p>CS 179J - Team6</p>
			<hr />

			<a
				href="https://www.kaggle.com/sudalairajkumar/novel-corona-virus-2019-dataset"
			>
				<button type="button" class="m-3 btn btn-dark">
					Data
					<font-awesome-icon :icon="['fas', 'database']" />
				</button>
			</a>
			<a href="https://github.com/szhan135/CS179J-Team6">
				<button type="button" class="m-3 btn btn-dark">
					Github
					<i class="fab fa-github"></i>
				</button>
			</a>
		</div>
		<TechUsed />
	</div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core';
import { faDatabase } from '@fortawesome/free-solid-svg-icons';
library.add(faDatabase);
export default {
	name: 'Home',
	data() {
		return {};
	},
	// ADDING THE DONUTCHART TO THE COMPONENTS OBJECT OF HOME
	components: { },
};
</script>

<style>
.padding {
	margin: 180px;
}
</style>